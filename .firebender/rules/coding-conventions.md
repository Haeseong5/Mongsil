# Coding Conventions

## Kotlin Style Guide

### Naming Conventions
- **Classes/Objects**: PascalCase (e.g., `UserRepository`, `MainActivity`)
- **Functions/Variables**: camelCase (e.g., `getUserData`, `isEnabled`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`)
- **Private properties**: camelCase with underscore prefix if needed (e.g., `_state`)

### Code Organization
- Keep files focused on a single responsibility
- Maximum file length: 300 lines (consider splitting if larger)
- Group related functions together
- Place companion objects at the end of the class

### Comments
- Write clear, concise comments explaining **why**, not **what**
- Use KDoc for public APIs
- Avoid redundant comments that restate the code
- Update comments when code changes

### Nullability
- Use nullable types (`?`) sparingly
- Prefer safe calls (`?.`) over `!!` operator
- Use `requireNotNull()` or `checkNotNull()` with descriptive messages
- Consider using `sealed class` for representing success/error states

### Functions
- Keep functions small and focused (ideally under 20 lines)
- Use descriptive function names that clearly indicate purpose
- Prefer expression body for simple functions
- Use default parameters instead of overloading when possible

### Example
```kotlin
// Good
fun calculateTotalPrice(items: List<Item>, discount: Double = 0.0): Double {
    require(discount in 0.0..1.0) { "Discount must be between 0 and 1" }
    return items.sumOf { it.price } * (1 - discount)
}

// Avoid
fun calcPrice(i: List<Item>): Double {
    var total = 0.0
    for (item in i) {
        total = total + item.price
    }
    return total
}
```
