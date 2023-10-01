package com.drylands.api.rest.resources;

import com.drylands.api.rest.dtos.response.ApiResponseDTO;
import com.drylands.api.rest.dtos.venda.ListagemVendaDTO;
import com.drylands.api.rest.dtos.venda.VendaCsvDTO;
import com.drylands.api.rest.dtos.venda.VendaDTO;
import com.drylands.api.services.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venda")
public class VendaController extends AbstractController {

    private final VendaService vendaService;

    public VendaController(ModelMapper modelMapper, VendaService vendaService) {
        super(modelMapper);
        this.vendaService = vendaService;
    }

    @PostMapping()
    @Operation(tags = "Venda", summary = "Registrar uma venda")
    public ResponseEntity<VendaDTO> criarVenda(@RequestBody VendaDTO vendaDTO) {
        return new ResponseEntity<>(mapearDTO(this.vendaService.criarVenda(vendaDTO), VendaDTO.class), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(tags = "Venda", summary = "Atualizar venda por id")
    public ResponseEntity<VendaDTO> atualizarVenda(@RequestBody VendaDTO vendaDto, @PathVariable("id") Long id) {
        return new ResponseEntity<>(mapearDTO(this.vendaService.atualizarVenda(id, vendaDto), VendaDTO.class), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Venda", summary = "Pegar venda por id")
    public ResponseEntity<VendaDTO> pegarVendaPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(mapearDTO(this.vendaService.pegarVendaPorId(id), VendaDTO.class), HttpStatus.OK);
    }

    @GetMapping()
    @Operation(tags = "Venda", summary = "Listar todas as vendas com paginação e filtro")
    public ResponseEntity<ListagemVendaDTO> listarVendas(@RequestParam(name = "clientId", required = false) Long clienteId, Pageable pageable) {
        return new ResponseEntity<>(this.vendaService.listarVendas(clienteId, pageable), HttpStatus.OK);
    }

    @GetMapping("/csv")
    @Operation(tags = "Venda", summary = "Listar todas as vendas")
    public ResponseEntity<List<VendaCsvDTO>> listarVendasSemPaginacao() {
        return new ResponseEntity<>(this.vendaService.listarVendasSemPaginacao(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(tags = "Venda", summary = "Deletar venda por id")
    public ResponseEntity<ApiResponseDTO> deletarVenda(@PathVariable("id") Long id) {
        this.vendaService.deletarVenda(id);
        return new ResponseEntity<>(new ApiResponseDTO(true, "Venda deletada com sucesso"), HttpStatus.OK);
    }
}