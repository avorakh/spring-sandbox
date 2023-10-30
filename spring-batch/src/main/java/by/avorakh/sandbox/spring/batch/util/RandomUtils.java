package by.avorakh.sandbox.spring.batch.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {

   private final Random random = new Random();

    public int nextInt(int bound){
        return random.nextInt(bound);
    }
}
