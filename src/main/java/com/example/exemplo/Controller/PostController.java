package com.example.exemplo.Controller;

import com.example.exemplo.Model.Post;
import com.example.exemplo.Model.Usuario;
import com.example.exemplo.Model.Repository.PostRepository;
import com.example.exemplo.Model.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Lista todos os posts */
    @GetMapping
    public List<Post> list() {
        return postRepository.findAll();
    }

    /** Encontra um post por Id */
    @GetMapping("/{id}")
    public ResponseEntity<Post> read(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Cria um novo post */
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        // Check unique title
        if (post.getTitle() != null) {
            Optional<Post> existing = postRepository.findByTitle(post.getTitle());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        // Validate / attach author if provided
        if (post.getAuthor() != null && post.getAuthor().getId() != null) {
            Optional<Usuario> optUsuario = usuarioRepository.findById(post.getAuthor().getId());
            if (optUsuario.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            post.setAuthor(optUsuario.get());
        }

        Post saved = postRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    /** Atualiza o post com base no id na requisição */
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post details) {
        Optional<Post> opt = postRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Post post = opt.get();

        // Check for title uniqueness if changed
        if (details.getTitle() != null && !details.getTitle().equals(post.getTitle())) {
            Optional<Post> byTitle = postRepository.findByTitle(details.getTitle());
            if (byTitle.isPresent() && !byTitle.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            post.setTitle(details.getTitle());
        }

        if (details.getContent() != null) {
            post.setContent(details.getContent());
        }

        if (details.getAuthor() != null && details.getAuthor().getId() != null) {
            Optional<Usuario> optUsuario = usuarioRepository.findById(details.getAuthor().getId());
            if (optUsuario.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            post.setAuthor(optUsuario.get());
        }

        Post saved = postRepository.save(post);
        return ResponseEntity.ok(saved);
    }

    /** Deleta o post com base no id na requisição */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Post> opt = postRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
