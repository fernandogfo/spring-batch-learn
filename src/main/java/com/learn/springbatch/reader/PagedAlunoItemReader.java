package com.learn.springbatch.reader;

import com.learn.springbatch.client.ExternalApiClient;
import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import com.learn.springbatch.dto.ApiResponseDTO;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.batch.item.ExecutionContext;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PagedAlunoItemReader extends AbstractItemStreamItemReader<AlunosSistemaExternoDTO> {

    private final ExternalApiClient apiClient;
    private final int pageSize;
    private final AtomicInteger currentPage = new AtomicInteger(1);
    private List<AlunosSistemaExternoDTO> currentPageItems;
    private final AtomicInteger currentItemIndex = new AtomicInteger(0);
    private volatile boolean endOfData = false;
    private final Lock lock = new ReentrantLock();

    private static final String CURRENT_PAGE_KEY = "currentPage";
    private static final String CURRENT_INDEX_KEY = "currentItemIndex";
    private static final String END_OF_DATA_KEY = "endOfData";

    public PagedAlunoItemReader(ExternalApiClient apiClient, int pageSize) {
        this.apiClient = apiClient;
        this.pageSize = pageSize;
        setName("pagedAlunoItemReader");
    }

    @Override
    public AlunosSistemaExternoDTO read() throws Exception {
        if (endOfData) {
            return null;
        }

        lock.lock();
        try {
            // Verifica se precisa carregar nova página
            if (currentPageItems == null || currentItemIndex.get() >= currentPageItems.size()) {
                if (!loadNextPage()) {
                    return null;
                }
            }

            // Obtém o próximo item
            return currentPageItems.get(currentItemIndex.getAndIncrement());
        } finally {
            lock.unlock();
        }
    }

    private boolean loadNextPage() throws Exception {

        if (endOfData) return false;

        int pageToLoad = currentPage.get();
        System.out.println("Carregando página: " + pageToLoad);

        ApiResponseDTO response = apiClient.getPage(pageToLoad, pageSize);

        // Verifica se terminou a paginação
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            endOfData = true;
            System.out.println("Fim da paginação alcançado");
            return false;
        }

        currentPageItems = response.getData();
        currentItemIndex.set(0);
        currentPage.incrementAndGet();

        return true;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey(CURRENT_PAGE_KEY)) {
            currentPage.set(executionContext.getInt(CURRENT_PAGE_KEY));
            currentItemIndex.set(executionContext.getInt(CURRENT_INDEX_KEY));
            endOfData = "true".equals(executionContext.getString(END_OF_DATA_KEY, "false"));
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt(CURRENT_PAGE_KEY, currentPage.get());
        executionContext.putInt(CURRENT_INDEX_KEY, currentItemIndex.get());
        executionContext.putString(END_OF_DATA_KEY, endOfData ? "true" : "false");
    }

    @Override
    public void close() throws ItemStreamException {
        lock.lock();
        try {
            currentPageItems = null;
            currentItemIndex.set(0);
        } finally {
            lock.unlock();
        }
    }
}