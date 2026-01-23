package edu.iCET.pricing;

import edu.iCET.model.dto.PriceResult;
import edu.iCET.model.entity.Event;
import edu.iCET.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class VipPriceStrategy implements PriceStrategy{
    @Override
    public PriceResult calculate(User user, Event event) {
        double basePrice = event.getBasePrice();

        // VIP rule
        if (event.isHighDemand()) {
            // No discount for high demand events
            return new PriceResult(basePrice, false);
        }

        // 10% discount
        double discountedPrice = basePrice * 0.9;
        return new PriceResult(discountedPrice, false);

    }
}
