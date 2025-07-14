package com.learn.springbatch.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public class AlunoPartitioner implements Partitioner {

    private final int totalPages;

    public AlunoPartitioner(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();

        int pagesPerPartition = totalPages / gridSize;
        int remainder = totalPages % gridSize;

        int start = 1;
        for (int i = 0; i < gridSize; i++) {
            int end = start + pagesPerPartition - 1;
            if (i == gridSize - 1) {
                end += remainder;  // adiciona o restante na última partição
            }

            ExecutionContext context = new ExecutionContext();
            context.putInt("startPage", start);
            context.putInt("endPage", end);
            partitions.put("partition" + i, context);

            start = end + 1;
        }

        return partitions;
    }
}