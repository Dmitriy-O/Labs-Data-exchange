package org.byovsiannikov.server.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.byovsiannikov.common.grpc.BookingProto.*;
import org.byovsiannikov.common.grpc.BookingServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Component
public class BookingServiceGrps {

    private Server server;
    private final int port = 9090;

    @PostConstruct
    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new BookingServiceImpl())
                .build()
                .start();
        System.out.println("gRPC сервер запущено на порту " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("выключено gRPC сервера...");
            BookingServiceGrps.this.stop();
            System.err.println("Сервер выключено.");
        }));
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private static class BookingServiceImpl extends BookingServiceGrpc.BookingServiceImplBase {

        private ConcurrentHashMap<String, Booking> bookings = new ConcurrentHashMap<>();

        @Override
        public void bookTicket(BookRequest request, StreamObserver<BookResponse> responseObserver) {
            String bookingId = UUID.randomUUID().toString();
            Booking booking = new Booking(
                    bookingId,
                    request.getUserId(),
                    request.getMovieId(),
                    request.getSeatNumber(),
                    "CONFIRMED"
            );
            bookings.put(bookingId, booking);
            BookResponse response = BookResponse.newBuilder()
                    .setSuccess(true)
                    .setBookingId(bookingId)
                    .setMessage("Успешное бронирование")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelBooking(CancelRequest request, StreamObserver<CancelResponse> responseObserver) {
            Booking booking = bookings.get(request.getBookingId());
            if (booking != null && booking.getStatus().equals("CONFIRMED")) {
                booking.setStatus("CANCELLED");
                bookings.put(request.getBookingId(), booking);
                CancelResponse response = CancelResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Бронирование отменено")
                        .build();
                responseObserver.onNext(response);
            } else {
                CancelResponse response = CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Бронирование не найдено или уже отменено")
                        .build();
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        }

        @Override
        public void getBooking(GetRequest request, StreamObserver<GetResponse> responseObserver) {
            Booking booking = bookings.get(request.getBookingId());
            if (booking != null) {
                GetResponse response = GetResponse.newBuilder()
                        .setBookingId(booking.getBookingId())
                        .setUserId(booking.getUserId())
                        .setMovieId(booking.getMovieId())
                        .setSeatNumber(booking.getSeatNumber())
                        .setStatus(booking.getStatus())
                        .build();
                responseObserver.onNext(response);
            } else {

                GetResponse response = GetResponse.newBuilder()
                        .setBookingId("")
                        .setUserId("")
                        .setMovieId("")
                        .setSeatNumber(0)
                        .setStatus("NOT_FOUND")
                        .build();
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        }
        @Override
        public void getAllBookings(EmptyRequest request, StreamObserver<GetAllResponse> responseObserver) {
            GetAllResponse.Builder responseBuilder = GetAllResponse.newBuilder();
            for (Booking booking : bookings.values()) {
                GetResponse bookingResponse = GetResponse.newBuilder()
                        .setBookingId(booking.getBookingId())
                        .setUserId(booking.getUserId())
                        .setMovieId(booking.getMovieId())
                        .setSeatNumber(booking.getSeatNumber())
                        .setStatus(booking.getStatus())
                        .build();
                responseBuilder.addBookings(bookingResponse);
            }
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }

        @Override
        public void updateBooking(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
            String bookingId = request.getBookingId();
            int newSeatNumber = request.getSeatNumber();

            Booking booking = bookings.get(bookingId);
            if (booking != null && booking.getStatus().equals("CONFIRMED")) {
                booking.setSeatNumber(newSeatNumber);
                bookings.put(bookingId, booking);

                UpdateResponse response = UpdateResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Бронирование обновлено")
                        .build();
                responseObserver.onNext(response);
            } else {
                UpdateResponse response = UpdateResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Бронирование не найдено или не может быть обновлено")
                        .build();
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        }

    }

    private static class Booking {
        private String bookingId;
        private String userId;
        private String movieId;
        private int seatNumber;
        private String status;

        public Booking(String bookingId, String userId, String movieId, int seatNumber, String status) {
            this.bookingId = bookingId;
            this.userId = userId;
            this.movieId = movieId;
            this.seatNumber = seatNumber;
            this.status = status;
        }

        public String getBookingId() {
            return bookingId;
        }

        public void setSeatNumber (int seatNumber) {
            this.seatNumber = seatNumber;
        }

        public String getUserId() {
            return userId;
        }

        public String getMovieId() {
            return movieId;
        }

        public int getSeatNumber() {
            return seatNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
