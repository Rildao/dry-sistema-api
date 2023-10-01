package com.drylands.api.rest.resources;

import com.drylands.api.rest.dtos.cliente.ClienteCsvDTO;
import com.drylands.api.rest.dtos.cliente.ClienteDTO;
import com.drylands.api.rest.dtos.cliente.ClienteVendasDTO;
import com.drylands.api.rest.dtos.cliente.ListagemClienteDTO;
import com.drylands.api.rest.dtos.response.ApiResponseDTO;
import com.drylands.api.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController extends AbstractController {

    private final ClienteService clienteService;

    public ClienteController(ModelMapper modelMapper, ClienteService clienteService) {
        super(modelMapper);
        this.clienteService = clienteService;
    }

    @PostMapping()
    @Operation(tags = "Cliente", summary = "Criar um cliente")
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody ClienteDTO clienteDTO) {
        return new ResponseEntity<>(mapearDTO(this.clienteService.criarCliente(clienteDTO), ClienteDTO.class), HttpStatus.OK);
    }

    @PostMapping("/venda")
    @Operation(tags = "Cliente", summary = "Criar cliente e relacionar a vendas")
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody ClienteVendasDTO clienteVendasDTO) {
        return new ResponseEntity<>(mapearDTO(this.clienteService.criarOuAtualizarClienteComVendas(clienteVendasDTO), ClienteDTO.class), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(tags = "Cliente", summary = "Atualizar cliente por id")
    public ResponseEntity<ClienteDTO> atualizarCliente(@RequestBody ClienteDTO clienteDTO, @PathVariable("id") Long id) {
        return new ResponseEntity<>(mapearDTO(this.clienteService.atualizarCliente(id, clienteDTO), ClienteDTO.class), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Cliente", summary = "Pegar cliente por id")
    public ResponseEntity<ClienteVendasDTO> pegarClientePorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.clienteService.pegarClientePorId(id), HttpStatus.OK);
    }

    @GetMapping()
    @Operation(tags = "Cliente", summary = "Listar todos os clientes paginados")
    public ResponseEntity<ListagemClienteDTO> listarClientes(Pageable pageable,
                                                             @RequestParam(value = "filter", required = false) String filter) {
        return new ResponseEntity<>(this.clienteService.listarClientes(pageable, filter), HttpStatus.OK);
    }

    @GetMapping("/csv")
    @Operation(tags = "Cliente", summary = "Listar todos os clientes")
    public ResponseEntity<List<ClienteCsvDTO>> listarClientesSemPaginacao() {
        return new ResponseEntity<>(this.clienteService.listarClientesSemPaginacao(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(tags = "Cliente", summary = "Deletar cliente por id")
    public ResponseEntity<ApiResponseDTO> deletarCliente(@PathVariable("id") Long id) {
        this.clienteService.deletarCliente(id);
        return new ResponseEntity<>(new ApiResponseDTO(true, "Cliente deletado com sucesso"), HttpStatus.OK);
    }
}