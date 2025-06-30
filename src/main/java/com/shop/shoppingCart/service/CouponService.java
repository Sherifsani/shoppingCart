package com.shop.shoppingCart.service;

import com.shop.shoppingCart.model.Coupon;
import com.shop.shoppingCart.repository.CouponRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @PostConstruct
    public void initDefaultCoupons() {
        // Create default POWERLABSx coupon if it doesn't exist
        if (!couponRepository.findByCode("POWERLABSx").isPresent()) {
            Coupon coupon = new Coupon(
                    "POWERLABSx",
                    13.2,
                    LocalDateTime.now().plusYears(1) // Valid for 1 year
            );
            couponRepository.save(coupon);
        }
    }

    public Optional<Coupon> validateCoupon(String code) {
        return couponRepository.findByCode(code)
                .filter(Coupon::isValid);
    }

    public double applyDiscount(double amount, String couponCode) {
        return validateCoupon(couponCode)
                .map(coupon -> {
                    double discount = amount * (coupon.getDiscountPercentage() / 100);
                    return amount - discount;
                })
                .orElse(amount);
    }
}
