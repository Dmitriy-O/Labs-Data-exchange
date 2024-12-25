package org.byovsiannikov.client.clientService;

import org.byovsiannikov.common.grpc.BookingProto.*;
import org.byovsiannikov.common.grpc.BookingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcBookingClient {

    private final BookingServiceGrpc.BookingServiceBlockingStub blockingStub;

    public GrpcBookingClient(
            @Value("${grpc.server.address}") String host,
            @Value("${grpc.server.port}") int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // У виробничому середовищі використовуйте TLS
                .build();
        blockingStub = BookingServiceGrpc.newBlockingStub(channel);
    }

    public BookResponse bookTicket(String userId, String movieId, int seatNumber) {
        BookRequest request = BookRequest.newBuilder()
                .setUserId(userId)
                .setMovieId(movieId)
                .setSeatNumber(seatNumber)
                .build();
        return blockingStub.bookTicket(request);
    }

    public CancelResponse cancelBooking(String bookingId) {
        CancelRequest request = CancelRequest.newBuilder()
                .setBookingId(bookingId)
                .build();
        return blockingStub.cancelBooking(request);
    }
    public GetAllResponse getAllBookings() {
        EmptyRequest request = EmptyRequest.newBuilder().build();
        return blockingStub.getAllBookings(request);
    }

    public GetResponse getBooking(String bookingId) {
        GetRequest request = GetRequest.newBuilder()
                .setBookingId(bookingId)
                .build();
        return blockingStub.getBooking(request);
    }
    public UpdateResponse updateBooking(String bookingId, int seatNumber) {
        UpdateRequest request = UpdateRequest.newBuilder()
                .setBookingId(bookingId)
                .setSeatNumber(seatNumber)
                .build();
        return blockingStub.updateBooking(request);
    }

    public DeleteResponse deleteBooking(String bookingId) {
        DeleteRequest request = DeleteRequest.newBuilder()
                .setBookingId(bookingId)
                .build();
        return blockingStub.deleteBooking(request);
    }

}
