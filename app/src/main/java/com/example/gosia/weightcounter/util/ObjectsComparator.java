package com.example.gosia.weightcounter.util;

public class ObjectsComparator {

    private ObjectsComparator() {
        throw new AssertionError("No objects instance");
    }

    public static boolean equals(Object firstObject, Object secondObject) {
        return (firstObject == secondObject) || (firstObject != null && firstObject.equals(secondObject));
    }

    private static boolean realEquals(Object firstObject, Object secondObject) {
        if (firstObject == secondObject) {
            return true;
        } else if (firstObject == null || secondObject == null) {
            return false;
        } else {
            return realEqualsObjects(firstObject, secondObject);
        }
    }

    private static boolean realEqualsObjects(Object firstObject, Object secondObject) {
        boolean ifEquals;
        assert firstObject != null;
        if (firstObject instanceof Object[] && secondObject instanceof Object[]) {
            ifEquals = realEquals(firstObject, secondObject);
        } else if (firstObject instanceof byte[] && secondObject instanceof byte[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof boolean[] && secondObject instanceof boolean[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof short[] && secondObject instanceof short[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof char[] && secondObject instanceof char[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof float[] && secondObject instanceof float[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof int[] && secondObject instanceof int[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof double[] && secondObject instanceof double[]) {
            ifEquals = equals(firstObject, secondObject);
        } else if (firstObject instanceof long[] && secondObject instanceof long[]) {
            ifEquals = equals(firstObject, secondObject);
        } else {
            ifEquals = firstObject.equals(secondObject);
        }
        return ifEquals;
    }
}
