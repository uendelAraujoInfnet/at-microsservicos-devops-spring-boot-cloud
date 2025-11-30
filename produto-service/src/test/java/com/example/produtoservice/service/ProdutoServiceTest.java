package com.example.produtoservice.service;

import com.example.produtoservice.dto.ProdutoDTO;
import com.example.produtoservice.model.Produto;
import com.example.produtoservice.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    private ProdutoRepository repository;
    private ProdutoService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProdutoRepository.class);
        service = new ProdutoService(repository);
    }

    @Test
    void deveListarTodos() {
        Produto p = new Produto(1L, "Ração", new BigDecimal("50.00"));
        when(repository.findAll()).thenReturn(List.of(p));

        List<ProdutoDTO> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Ração", resultado.get(0).getNome());
        assertEquals(new BigDecimal("50.00"), resultado.get(0).getPreco());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        Produto p = new Produto(1L, "Ração", new BigDecimal("50.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        ProdutoDTO dto = service.buscarPorId(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Ração", dto.getNome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.buscarPorId(1L));

        assertTrue(ex.getMessage().contains("Produto não encontrado"));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveCriarProduto() {
        Produto salvo = new Produto(1L, "Ração", new BigDecimal("50.00"));
        when(repository.save(ArgumentMatchers.any(Produto.class))).thenReturn(salvo);

        ProdutoDTO dto = new ProdutoDTO(null, "Ração", new BigDecimal("50.00"));

        ProdutoDTO resultado = service.criar(dto);

        assertNotNull(resultado.getId());
        assertEquals("Ração", resultado.getNome());
        assertEquals(new BigDecimal("50.00"), resultado.getPreco());
        verify(repository, times(1)).save(any(Produto.class));
    }
}
