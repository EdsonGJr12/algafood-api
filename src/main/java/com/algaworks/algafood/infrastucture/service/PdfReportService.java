package com.algaworks.algafood.infrastucture.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;
import com.algaworks.algafood.infrastucture.exception.ReportException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfReportService implements VendaReportService {
	
	@Autowired
	private VendaQueryService vendaQueryService;

	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filter, String timeOffset) {
		
		try {
			InputStream templateRelatorioInputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");	
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			List<VendaDiaria> vendasDiarias = vendaQueryService.consultarVendasDiarias(filter, timeOffset);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(vendasDiarias);

			JasperPrint jasperPrint = JasperFillManager.fillReport(templateRelatorioInputStream, parameters, dataSource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);
			
		} catch (JRException e) {
			throw new ReportException("Não foi possível emitir relatórios de vendas diárias", e);
		}
	}
}
