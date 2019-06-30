package pl.coderslab.chirper.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurseWordsValidator implements ConstraintValidator<NoSwearing, String> {
   public void initialize(NoSwearing constraint) {

   }

   public boolean isValid(String value, ConstraintValidatorContext context) {
      boolean contains = false;
      String[] swearWord = new String[]{"chuj", "dupa"};
      for (String s : swearWord) {
         if(value.contains(s.subSequence(0, s.length()))){
            contains = true;
         }
         System.out.println(contains + s);
         System.out.println(s.subSequence(0,s.length()));
      }
      return !contains;
   }
}
