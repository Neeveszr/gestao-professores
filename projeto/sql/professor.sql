CREATE TABLE professor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(150),
    area VARCHAR(100),
    telefone VARCHAR(20)
);

INSERT INTO professor (nome, email, area, telefone) VALUES
    ('João da Silva', 'joao.silva@example.com', 'Desenvolvimento', '86999999991'),
    ('João Pedro', 'joao.pedro@example.com', 'Engenharia de Software', '86999999992'),
    ('MARIA JOANA', 'maria.joana@example.com', 'Desenvolvimento', '86999999993');
