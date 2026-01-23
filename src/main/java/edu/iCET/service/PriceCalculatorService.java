package edu.iCET.service;

import edu.iCET.model.dto.PriceResult;
import edu.iCET.model.entity.Event;
import edu.iCET.model.entity.User;
import edu.iCET.pricing.PlatinumPriceStrategy;
import edu.iCET.pricing.RegularPriceStrategy;
import edu.iCET.pricing.VipPriceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceCalculatorService {

    @Autowired
    private RegularPriceStrategy regularPriceStrategy;

    @Autowired
    private VipPriceStrategy vipPriceStrategy;

    @Autowired
    private PlatinumPriceStrategy platinumPriceStrategy;

    public PriceResult calculatePrice(User user, Event event) {

        return switch (user.getTier()) {
            case "VIP" -> vipPriceStrategy.calculate(user, event);
            case "PLATINUM" -> platinumPriceStrategy.calculate(user, event);
            default -> regularPriceStrategy.calculate(user, event);
        };
    }

}
