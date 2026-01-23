package edu.iCET.pricing;

import edu.iCET.model.dto.PriceResult;
import edu.iCET.model.entity.Event;
import edu.iCET.model.entity.User;

public interface PriceStrategy {
    PriceResult calculate(User user, Event event);
}
