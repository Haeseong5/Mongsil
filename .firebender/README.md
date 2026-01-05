# Firebender Rules for Mongsil Project

This directory contains project-specific guidelines and conventions for AI-assisted development.

## Purpose

These rules help Firebender (and other AI coding assistants) understand:
- Project architecture and design patterns
- Coding conventions and style preferences
- Platform-specific considerations (KMP)
- Best practices and common pitfalls to avoid

## Rules Files

1. **`coding-conventions.md`**: Kotlin coding style, naming conventions, and code organization
2. **`kmp-guidelines.md`**: Kotlin Multiplatform specific guidelines and best practices
3. **`architecture.md`**: Project architecture, layering, and design patterns
4. **`dependencies.md`**: Dependency management and library selection guidelines

## How to Use

### For Developers
- Review these rules when onboarding to the project
- Reference them when making architectural decisions
- Update rules as the project evolves

### For AI Assistants
- These rules are automatically loaded by Firebender
- Follow guidelines when generating or modifying code
- Prioritize these rules over generic best practices

## Updating Rules

When updating rules:
1. Keep rules concise and actionable
2. Include code examples for clarity
3. Document the "why" behind conventions
4. Update this README if adding new rule files

## Best Practices for Writing Rules

### ✅ Good Rules
- Specific and actionable
- Include concrete examples
- Explain reasoning when non-obvious
- Focus on project-specific needs

### ❌ Avoid
- Generic advice that applies to all projects
- Overly prescriptive rules that limit flexibility
- Contradictory guidelines
- Rules that are never enforced

## Project Context

**Project**: Mongsil - Kotlin Multiplatform app
**Platforms**: Android, iOS
**Architecture**: Multi-module with clean architecture
**UI**: Compose Multiplatform (shared UI)
