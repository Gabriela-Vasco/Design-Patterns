package one.digitalinnovation.labpadroesprojetospring.service;

import one.digitalinnovation.labpadroesprojetospring.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio do cliente. Com isso, se necessário, podemos ter mais de uma
 * implementação dessa mesma interface.
 *
 * @author Gabriela
 */
public interface ClienteService {

    Iterable<Cliente> findAll();

    Cliente findById(Long id);

    void create(Cliente cliente);

    void update(Long id, Cliente cliente);

    void delete(Long id);

}
