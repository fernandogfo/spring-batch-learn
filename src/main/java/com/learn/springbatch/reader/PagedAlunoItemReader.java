package com.learn.springbatch.reader;

import com.learn.springbatch.client.ExternalApiClient;
import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import org.springframework.batch.item.ItemReader;

public class PagedAlunoItemReader implements ItemReader<AlunosSistemaExternoDTO> {

    private final ExternalApiClient apiClient;
    private int currentPage;
    private final int endPage;

    public PagedAlunoItemReader(ExternalApiClient apiClient, int startPage, int endPage) {
        this.apiClient = apiClient;
        this.currentPage = startPage;
        this.endPage = endPage;
        System.out.println("Instanciando PagedAlunoItemReader com startPage=" + startPage + ", endPage=" + endPage);

    }

    @Override
    public AlunosSistemaExternoDTO read() throws Exception {
        if (currentPage > endPage) {
            return null;
        }
        AlunosSistemaExternoDTO item = apiClient.getPage(currentPage);
        currentPage++;
        System.out.println("Criando reader para páginas " + currentPage + " até " + endPage);
        return item;
    }
}
