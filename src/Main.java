/***********************************************************************************
 * Universidade Católica de Brasília - UCB                                         *
 * Disciplina: Programação Orientada a Objetos                                     *
 * Professor: Alexandre S. D. Santos                                               *
 * Data: 10/04/2026                                                                *
 *                                                                                 *
 * Descrição: Sistema de biblioteca simples                                        *   
 ***********************************************************************************/

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

// ================= LIVRO =================
class Livro {
    private int id;
    private String titulo;
    private boolean disponivel;

    public Livro(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.disponivel = true;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isDisponivel() { return disponivel; }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}

// ================= USUARIO =================
abstract class Usuario {
    private int id;
    private String nome;
    protected int qtdEmprestimos;

    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.qtdEmprestimos = 0;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getQtdEmprestimos() { return qtdEmprestimos; }

    public void adicionarEmprestimo() {
        qtdEmprestimos++;
    }

    public void removerEmprestimo() {
        qtdEmprestimos--;
    }

    public abstract int getLimite();
}

// ================= ALUNO =================
class Aluno extends Usuario {
    public Aluno(int id, String nome) {
        super(id, nome);
    }

    @Override
    public int getLimite() {
        return 3;
    }
}

// ================= PROFESSOR =================
class Professor extends Usuario {
    public Professor(int id, String nome) {
        super(id, nome);
    }

    @Override
    public int getLimite() {
        return 5;
    }
}

// ================= EMPRESTIMO =================
class Emprestimo {
    private Livro livro;
    private Usuario usuario;
    private LocalDate data;

    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.data = LocalDate.now();
    }

    public Livro getLivro() { return livro; }
    public Usuario getUsuario() { return usuario; }

    @Override
    public String toString() {
        return "Livro: " + livro.getTitulo() +
                " | Usuario: " + usuario.getNome() +
                " | Data: " + data;
    }
}

// ================= BIBLIOTECA =================
class Biblioteca {
    private ArrayList<Livro> livros = new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Emprestimo> emprestimos = new ArrayList<>();

    public void cadastrarLivro(Livro livro) {
        livros.add(livro);
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    private Livro buscarLivro(int id) {
        for (Livro l : livros) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    private Usuario buscarUsuario(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    public void emprestarLivro(int idLivro, int idUsuario) {
        Livro l = buscarLivro(idLivro);
        Usuario u = buscarUsuario(idUsuario);

        if (l == null || u == null) {
            System.out.println("Livro ou usuario nao encontrado.");
            return;
        }

        if (!l.isDisponivel()) {
            System.out.println("Livro ja emprestado.");
            return;
        }

        if (u.getQtdEmprestimos() >= u.getLimite()) {
            System.out.println("Limite de emprestimos atingido.");
            return;
        }

        Emprestimo e = new Emprestimo(l, u);
        emprestimos.add(e);

        l.setDisponivel(false);
        u.adicionarEmprestimo();

        System.out.println("Emprestimo realizado!");
    }

    public void devolverLivro(int idLivro) {
        Emprestimo encontrado = null;

        for (Emprestimo e : emprestimos) {
            if (e.getLivro().getId() == idLivro) {
                encontrado = e;
                break;
            }
        }

        if (encontrado != null) {
            encontrado.getLivro().setDisponivel(true);
            encontrado.getUsuario().removerEmprestimo();

            emprestimos.remove(encontrado);
            System.out.println("Livro devolvido!");
        } else {
            System.out.println("Emprestimo nao encontrado.");
        }
    }

    public void listarDisponiveis() {
        System.out.println("\n--- Livros Disponiveis ---");
        for (Livro l : livros) {
            if (l.isDisponivel()) {
                System.out.println(l.getId() + " - " + l.getTitulo());
            }
        }
    }

    public void listarEmprestimos() {
        System.out.println("\n--- Emprestimos ---");
        for (Emprestimo e : emprestimos) {
            System.out.println(e);
        }
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {

        Biblioteca biblioteca = new Biblioteca();
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== BIBLIOTECA =====");
            System.out.println("1 - Cadastrar livro");
            System.out.println("2 - Cadastrar usuario");
            System.out.println("3 - Emprestar livro");
            System.out.println("4 - Devolver livro");
            System.out.println("5 - Listar livros disponiveis");
            System.out.println("6 - Listar emprestimos");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("ID: ");
                    int idLivro = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Titulo: ");
                    String titulo = sc.nextLine();

                    biblioteca.cadastrarLivro(new Livro(idLivro, titulo));
                    break;

                case 2:
                    System.out.print("ID: ");
                    int idUsuario = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Tipo (1-Aluno | 2-Professor): ");
                    int tipo = sc.nextInt();

                    if (tipo == 1)
                        biblioteca.cadastrarUsuario(new Aluno(idUsuario, nome));
                    else
                        biblioteca.cadastrarUsuario(new Professor(idUsuario, nome));
                    break;

                case 3:
                    System.out.print("ID Livro: ");
                    int l = sc.nextInt();

                    System.out.print("ID Usuario: ");
                    int u = sc.nextInt();

                    biblioteca.emprestarLivro(l, u);
                    break;

                case 4:
                    System.out.print("ID Livro: ");
                    int d = sc.nextInt();

                    biblioteca.devolverLivro(d);
                    break;

                case 5:
                    biblioteca.listarDisponiveis();
                    break;

                case 6:
                    biblioteca.listarEmprestimos();
                    break;
            }

        } while (opcao != 0);

        sc.close();
    }
}
