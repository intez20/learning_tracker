package com.tracker.repository;

import com.tracker.model.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, UUID> {
    
    List<CodeSnippet> findByProjectId(UUID projectId);
    
    List<CodeSnippet> findByLanguage(String language);
    
    @Query(value = "SELECT * FROM code_snippets WHERE :tag = ANY(tags)", nativeQuery = true)
    List<CodeSnippet> findByTagsContaining(@Param("tag") String tag);
    
    List<CodeSnippet> findByTitleContainingOrDescriptionContaining(String titleKeyword, String descriptionKeyword);
}
