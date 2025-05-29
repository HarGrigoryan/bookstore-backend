package com.example.bookstore.controller;

import com.example.bookstore.service.CouponService;
import com.example.bookstore.service.dto.CouponDTO;
import com.example.bookstore.service.dto.CouponRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('ANY', 'VIEW_COUPON')")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @GetMapping
    @PreAuthorize("hasPermission('ANY', 'VIEW_COUPON')")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('ROLE_MANAGER', 'REMOVE_COUPON')")
    public ResponseEntity<Void> deleteCouponById(@PathVariable Long id) {
        couponService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasPermission('ANY', 'CREATE_COUPON')")
    public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponRequestDTO coupon) {
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('ANY', 'UPDATE_COUPON')")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @RequestBody CouponRequestDTO coupon) {
        return ResponseEntity.ok(couponService.updateCoupon(id, coupon));
    }
}
