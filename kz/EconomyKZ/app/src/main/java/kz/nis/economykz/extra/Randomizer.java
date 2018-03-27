//package kz.nis.economy.extra;
//
///**
// * Created by myrza on 8/27/15.
// */
//public class Randomizer {
//
//
//    public static int[] randomizeTag(int arrayLength){
//
//        int[] tags = new int[arrayLength];
//        for (int i = 0; i < arrayLength; i++) {
//            tags[i] = i;
//        }
////        tags = randomGenerate(tags, tags.length);
//        return tags;
//    }
//
//    public static int[] randomizeTag(int[] array){
//
//       return randomGenerate(array,array.length);
//    }
//
//
//    private static int[] randomGenerate(int[] array, int quantity) {
//        if (array.length < quantity)
//            return null;
//        int[] arrayFinal = new int[quantity];
//        int index = 0, buf;
//        for (int i = 0; i < quantity; i++) {
//            buf = array[i];
//            index = rand(i, array.length - 1);
//            array[i] = array[index];
//            array[index] = buf;
//            arrayFinal[i] = array[i];
//        }
//        return arrayFinal;
//    }
//
//    private static int rand(int minValue, int maxValue) {
//        if (minValue > maxValue)
//            return 0;
//        maxValue = (maxValue - minValue + 1);
//        int rand = (int) (Math.random() * maxValue + minValue);
//        return rand;
//    }
//
//}
