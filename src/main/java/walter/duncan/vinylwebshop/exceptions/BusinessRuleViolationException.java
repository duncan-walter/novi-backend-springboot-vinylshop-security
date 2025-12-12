package walter.duncan.vinylwebshop.exceptions;

public class BusinessRuleViolationException extends RuntimeException {
    private final BusinessRuleViolation code;

    public BusinessRuleViolationException(BusinessRuleViolation violation, String message) {
        super(message);
        this.code = violation;
    }

    public BusinessRuleViolation getCode() {
        return this.code;
    }
}