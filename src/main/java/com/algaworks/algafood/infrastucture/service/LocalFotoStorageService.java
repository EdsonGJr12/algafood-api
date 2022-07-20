package com.algaworks.algafood.infrastucture.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.infrastucture.exception.StorageException;

@Service
public class LocalFotoStorageService implements FotoStorageService {
	
	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorioLocal;

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			OutputStream outputStream = Files.newOutputStream(arquivoPath);
			FileCopyUtils.copy(novaFoto.getInputStream(), outputStream);
		} catch (IOException e) {
			throw new StorageException("Não foi possível armazenar o arquivo", e);
		}
	}

	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			Files.deleteIfExists(arquivoPath);
		} catch (IOException e) {
			throw new StorageException("Não foi possível excluir o arquivo", e);
		}
	}

	@Override
	public InputStream recuperar(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			return Files.newInputStream(arquivoPath);
		} catch (IOException e) {
			throw new StorageException("Não foi possível recuperar o arquivo", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		return diretorioLocal.resolve(Path.of(nomeArquivo));
	}

}
