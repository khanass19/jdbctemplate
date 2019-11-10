package accelerator.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ListPartitionDivider {

    public static <T> Collection<List<T>> divideListIntoPartitionsOfSize(final List<T> inputList,
                                                                         final int size) {

        final AtomicInteger counter = new AtomicInteger(0);

        return inputList.stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / size))
                .values();
    }

}
