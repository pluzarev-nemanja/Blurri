# Blurri 🌟  
A modern image loading library for Jetpack Compose with built-in **blur effect** and **async image loading**. Blurri simplifies image handling in your Compose applications with placeholders, error handling, and an optional blur effect for a sleek user experience.

---

## ✨ Features
- 🖼️ Load images asynchronously from URLs, resources, or files.
- 🔄 Support for placeholders, error images, and fallback options.
- 💨 Add a customizable blur effect to images seamlessly.
- ⚡ Built specifically for Jetpack Compose.
- 🎨 Clean and modular architecture for easy extension.

---

## 🔧 Technologies Used
- **Jetpack Compose**: For building UI declaratively.
- **Kotlin Coroutines**: For asynchronous image loading.
- **Android Graphics**: For applying blur effects efficiently.
- **Painter Abstraction**: To handle dynamic image sources.
- **Custom Extensions**: Simplify applying effects like blur to bitmaps.

---

<h2>📸 Example Usage</h2>

<p>Here’s how to use <strong>Blurri</strong> to load and blur an image:</p>

<h3>Basic Usage</h3>
<pre>
<code class="language-kotlin">
BlurriImage(
    model = "https://example.com/image.jpg",
    contentDescription = "Blurred Image",
    blurRadius = 15f, // Apply a blur effect with a 15px radius
    placeholder = "https://example.com/loading.jpg",
    error = "https://example.com/error.jpg"
)
</code>
</pre>

<h3>Placeholder and Error Images</h3>
<p>You can use any type of image for the placeholder and error states. For example:</p>
<pre>
<code class="language-kotlin">
BlurriImage(
    model = "https://example.com/nonexistent.jpg",
    placeholder = File("/path/to/loading_image.jpg"),
    error = R.drawable.error_image,
    contentDescription = "Fallback Example"
)
</code>
</pre>

<h3>Advanced Options</h3>
<pre>
<code class="language-kotlin">
BlurriImage(
    model = myBitmap,
    blurRadius = 25f,
    placeholder = InputStream(FileInputStream("path/to/loading_image.png")),
    error = null, // No fallback for error state
    modifier = Modifier.size(200.dp),
    contentScale = ContentScale.FillBounds,
    colorFilter = ColorFilter.tint(Color.Red)
)
</code>
</pre>

## 📜 Installation
Add the following to your project’s `build.gradle` or `build.gradle.kts`:

1. Add it in your root build.gradle at the end of repositories:
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
 ```
### Groovy DSL
```groovy
implementation 'com.github.pluzarev-nemanja:Blurri:1.0'
```
### Kotlin DSL
```kotlin
implementation("com.github.pluzarev-nemanja:Blurri:1.0")

