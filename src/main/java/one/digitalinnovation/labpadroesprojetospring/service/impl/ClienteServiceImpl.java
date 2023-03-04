package one.digitalinnovation.labpadroesprojetospring.service.impl;

import one.digitalinnovation.labpadroesprojetospring.model.Cliente;
import one.digitalinnovation.labpadroesprojetospring.model.ClienteRepository;
import one.digitalinnovation.labpadroesprojetospring.model.Endereco;
import one.digitalinnovation.labpadroesprojetospring.model.EnderecoRepository;
import one.digitalinnovation.labpadroesprojetospring.service.ClienteService;
import one.digitalinnovation.labpadroesprojetospring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da Strategy, a qual pode ser injetada pelo Spring via Autowired. Com isso, como essa classe é um
 * Service, ela será tratada como um Singleton.
 *
 * @author Gabriela
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    //Singleton: Injetar os componentes do Spring com @Autowired
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;
    //Strategy: Implementar os métodos definidos na interface
    //Facade: Abstrair integrações com subsistemas, provendo uma interface simples

    @Override
    public Iterable<Cliente> findAll() {
        //Buscar todos os clientes
        return clienteRepository.findAll();
    }

    @Override
    public Cliente findById(Long id) {
        //Buscar um cliente por ID
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()){
            return cliente.get();
        } else {
            return null;
        }
    }

    private void salvarClienteComCep(Cliente cliente) {
        //Verificar se o Endereço do Cliente já existe (pelo CEP)
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            //Caso não exista, integra com o ViaCEP e persistir o retorno
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //Inserir ou lterar Cliente, vinculando o Endereço (novo ou existente)
        clienteRepository.save(cliente);
    }

    @Override
    public void create(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void update(Long id, Cliente cliente) {
        //Buscar Cliente por ID, caso exista
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
