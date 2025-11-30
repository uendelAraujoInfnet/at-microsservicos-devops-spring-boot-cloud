package com.example.produtoservice.service;

import com.example.produtoservice.dto.ProdutoDTO;
import com.example.produtoservice.model.Produto;
import com.example.produtoservice.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
        return toDTO(produto);
    }

    @Transactional
    public ProdutoDTO criar(ProdutoDTO dto) {
        Produto p = new Produto();
        p.setNome(dto.getNome());
        p.setPreco(dto.getPreco());
        Produto salvo = repository.save(p);
        return toDTO(salvo);
    }

    private ProdutoDTO toDTO(Produto p) {
        return ProdutoDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .preco(p.getPreco())
                .build();
    }
}
