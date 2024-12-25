package org.byovsiannikov.client.controller;

import com.google.protobuf.util.JsonFormat;
import org.byovsiannikov.client.clientService.GrpcBookingClient;
import org.byovsiannikov.common.grpc.BookingProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final GrpcBookingClient grpcBookingClient;

    @Autowired
    public BookingController(GrpcBookingClient grpcBookingClient) {
        this.grpcBookingClient = grpcBookingClient;
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookTicket(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "movieId") String movieId,
            @RequestParam(name = "seatNumber") int seatNumber) {
        try {
            BookingProto.BookResponse response = grpcBookingClient.bookTicket(userId, movieId, seatNumber);
            String json = JsonFormat.printer().print(response);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestParam(name = "bookingId") String bookingId) {
        try {
            BookingProto.CancelResponse response = grpcBookingClient.cancelBooking(bookingId);
            String json = JsonFormat.printer().print(response);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<String> getBooking(@PathVariable(name = "bookingId") String bookingId) {
        try {
            BookingProto.GetResponse response = grpcBookingClient.getBooking(bookingId);
            String json = JsonFormat.printer().print(response);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping("/{bookingId}")
    public ResponseEntity<String> updateBooking(
            @PathVariable(name = "bookingId") String bookingId,
            @RequestParam(name = "seatNumber") int seatNumber) {
        try {
            BookingProto.UpdateResponse response = grpcBookingClient.updateBooking(bookingId, seatNumber);
            String json = JsonFormat.printer().print(response);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<String> getAllBookings() {
        try {
            BookingProto.GetAllResponse response = grpcBookingClient.getAllBookings();
            String json = JsonFormat.printer().print(response);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable(name = "bookingId") String bookingId) {
        try {
            BookingProto.DeleteResponse response = grpcBookingClient.deleteBooking(bookingId);
            String json = JsonFormat.printer().print(response);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
