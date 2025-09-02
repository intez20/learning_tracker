package com.tracker.service;

import com.tracker.model.CodeSnippet;
import com.tracker.repository.CodeSnippetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodeSnippetService {

    private final CodeSnippetRepository codeSnippetRepository;

    public List<CodeSnippet> getAllCodeSnippets() {
        return codeSnippetRepository.findAll();
    }

    public Optional<CodeSnippet> getCodeSnippetById(UUID id) {
        return codeSnippetRepository.findById(id);
    }

    public CodeSnippet createCodeSnippet(CodeSnippet codeSnippet) {
        return codeSnippetRepository.save(codeSnippet);
    }

    public Optional<CodeSnippet> updateCodeSnippet(UUID id, CodeSnippet snippetDetails) {
        return codeSnippetRepository.findById(id)
                .map(existingSnippet -> {
                    existingSnippet.setTitle(snippetDetails.getTitle());
                    existingSnippet.setDescription(snippetDetails.getDescription());
                    existingSnippet.setCode(snippetDetails.getCode());
                    existingSnippet.setLanguage(snippetDetails.getLanguage());
                    existingSnippet.setTags(snippetDetails.getTags());
                    existingSnippet.setProjectId(snippetDetails.getProjectId());
                    return codeSnippetRepository.save(existingSnippet);
                });
    }

    public boolean deleteCodeSnippet(UUID id) {
        return codeSnippetRepository.findById(id)
                .map(snippet -> {
                    codeSnippetRepository.delete(snippet);
                    return true;
                })
                .orElse(false);
    }

    public List<CodeSnippet> getCodeSnippetsByProject(UUID projectId) {
        return codeSnippetRepository.findByProjectId(projectId);
    }

    public List<CodeSnippet> getCodeSnippetsByLanguage(String language) {
        return codeSnippetRepository.findByLanguage(language);
    }

    public List<CodeSnippet> getCodeSnippetsByTag(String tag) {
        return codeSnippetRepository.findByTagsContaining(tag);
    }

    public List<CodeSnippet> searchCodeSnippets(String keyword) {
        return codeSnippetRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
    }
}
