package com.example.bookstore.service;

import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.ResourceAlreadyUsedException;
import com.example.bookstore.persistance.entity.Coupon;
import com.example.bookstore.persistance.repository.CouponRepository;
import com.example.bookstore.service.dto.CouponDTO;
import com.example.bookstore.service.dto.CouponRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;


    public CouponDTO getCouponById(Long id) {
        return CouponDTO.toDTO(couponRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon", id)));
    }


    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll().stream().map(CouponDTO::toDTO).collect(Collectors.toList());
    }


    public void deleteById(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon", id));
        couponRepository.delete(coupon);
    }

    public CouponDTO createCoupon(CouponRequestDTO couponDTO) {
        String code = couponDTO.getCode();
        Coupon coupon = couponRepository.findByCode(code).orElse(null);
        if(coupon != null) {

            throw new ResourceAlreadyUsedException("Coupon with code '%s' already exists".formatted(code));
        }
        coupon = new Coupon();
        mapCouponRequestToEntity(couponDTO, coupon);
        return CouponDTO.toDTO(couponRepository.save(coupon));
    }

    public CouponDTO updateCoupon(Long id, CouponRequestDTO couponDTO) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon", id));
        mapCouponRequestToEntity(couponDTO, coupon);
        return CouponDTO.toDTO(couponRepository.save(coupon));
    }

    private static void mapCouponRequestToEntity(CouponRequestDTO couponDTO, Coupon coupon) {
        coupon.setDescription(couponDTO.getDescription());
        coupon.setCode(couponDTO.getCode());
        coupon.setStartDate(couponDTO.getStartDate());
        coupon.setEndDate(couponDTO.getEndDate());
        coupon.setUsageLimit(couponDTO.getUsageLimit());
        coupon.setUsageLimitPerUser(couponDTO.getUsageLimitPerUser());
        coupon.setDiscountPercentage(couponDTO.getDiscountPercentage());
    }
}
