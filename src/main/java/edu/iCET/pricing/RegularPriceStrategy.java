package edu.iCET.pricing;

import edu.iCET.model.dto.PriceResult;
import edu.iCET.model.entity.Event;
import edu.iCET.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class RegularPriceStrategy implements PriceStrategy{

    @Override
    public PriceResult calculate(User user, Event event) {
        return new PriceResult(
                event.getBasePrice(),
                false
        );
    }
}
