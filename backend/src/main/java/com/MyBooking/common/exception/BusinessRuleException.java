public class BusinessRuleException extends RuntimeException {
   // Error message only
   public BusinessRuleException(String message) {
    super(message);
   }
   // Error message and cause
   public BusinessRuleException(String message, Throwable cause) {
    super(message, cause);
   }
 }

