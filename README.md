# 🧵 Concurrent Programming Laboratory - Prime Number Finder (ARSW)

## 👥 Team Members

- [Jesús Alfonso Pinzón Vega](https://github.com/JAPV-X2612)
- [David Felipe Velásquez Contreras](https://github.com/DavidVCAI)

---

## 📚 **Laboratory Overview**

This laboratory focuses on **concurrent programming**, **race conditions**, and **thread synchronization** in *Java*. The main objectives include learning to create, coordinate, and synchronize multiple threads while exploring the performance benefits of parallelization.

### 🎯 **Learning Objectives**

- ✅ Understanding **thread creation** and lifecycle management
- ✅ Implementing **thread coordination** using `join()` methods
- ✅ Exploring **CPU core utilization** through parallel processing
- ✅ Implementing **pause/resume functionality** with synchronization primitives
- ✅ Analyzing **performance improvements** through multithreading

---

## ⚙️ **Prerequisites & Setup**

### 🔧 **Maven Configuration**

To easily execute the project with `mvn`, we added the **exec-maven-plugin** to the `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <mainClass>edu.eci.arsw.primefinder.Main</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### ⚡ **Quick Execution Command**

Execute the project using:

```bash
mvn clean compile exec:java
```

---

## 🎯 **Part I: Concurrent Prime Number Finder**

### 📋 **Initial Analysis - Single Thread Execution**

#### 🔍 **Point 1: Baseline Performance**

We executed the original prime finder program to establish baseline performance metrics:

```bash
mvn clean compile exec:java
```

**System Resource Analysis:**

<img src="assets/images/initial_prime_finder_thread_execution.png" alt="Initial PrimeFinderThread Execution" width="60%">

<img src="assets/images/initial_prime_finder_thread_results.png" alt="Initial PrimeFinderThread Results" width="40%">

**Key Observations:**
- 🔄 **Single thread execution** limits CPU utilization
- 📊 **Partial core usage** across available processors
- ⏱️ **Extended execution time** due to sequential processing
- 💻 **Underutilized hardware resources** in multi-core systems

---

### 🚀 **Point 2: Multi-Threading Implementation**

#### 📈 **Performance Improvement with 3 Threads**

After modifying the program to utilize **3 parallel threads** instead of one:

**Thread Distribution:**
- 🧵 **Thread 1**: Range `0` to `9,999,999`
- 🧵 **Thread 2**: Range `10,000,000` to `19,999,999`
- 🧵 **Thread 3**: Range `20,000,000` to `30,000,000`

**Performance Results:**

<img src="assets/images/current_prime_finder_thread_execution.png" alt="Current PrimeFinderThread Execution" width="60%">

<img src="assets/images/current_prime_finder_thread_results.png" alt="Current PrimeFinderThread Results" width="40%">

**Improvements Achieved:**
- ⚡ **Significantly reduced execution time**
- 🔄 **Enhanced CPU core utilization**
- 📊 **Better resource distribution** across available processors
- 🎯 **True parallel processing** implementation

---

### ⏸️ **Point 3: Pause/Resume Functionality**

#### 🛠️ **Thread Synchronization Implementation**

We implemented a **pause and resume mechanism** that automatically stops execution every **5 seconds** and waits for user input (*ENTER*) to continue:

**Key Components:**

##### 🎮 **ThreadController Class**
- **Purpose**: Manages thread synchronization using `wait()` and `notifyAll()`
- **Features**: 
  - 🔒 Thread-safe pause/resume operations
  - 📡 Shared monitor for all worker threads
  - 🔄 Periodic pause point checking

##### ⏱️ **Timer-Based Execution Control**
- **Automatic Pause**: Every `5000ms` (5 seconds)
- **User Interaction**: Press *ENTER* to resume execution
- **Recursive Scheduling**: Continues until all threads complete

**Execution Flow:**

<img src="assets/images/thread_stop.png" alt="Thread Stop" width="50%">

**Synchronization Features:**
- 🔄 **Recursive timer scheduling** for continuous pause cycles
- 📊 **Progress reporting** during each pause
- ⌨️ **User-controlled resumption** via console input
- 🛡️ **Thread-safe state management**

---

## 📊 **Performance Analysis**

### 🔍 **CPU Utilization Comparison**

| **Configuration** | **Thread Count** | **CPU Cores Used** | **Execution Time** | **Performance Gain** |
|:-----------------:|:----------------:|:------------------:|:------------------:|:--------------------:|
| Single Thread     | 1                | ~1                 | Baseline           | -                    |
| Multi-Thread      | 3                | Multiple           | ~60% faster        | **⬆️ 2.5x improvement** |

### 🎯 **Key Performance Insights**

- **Scalability**: Linear performance improvement with thread count up to core limits
- **Resource Utilization**: Better distribution of computational load
- **Responsiveness**: Maintains system responsiveness through pause/resume functionality
- **Efficiency**: Optimal thread count correlates with available CPU cores

---

## 🏗️ **Architecture & Design**

### 📁 **Project Structure**

```
part1/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── edu/
                └── eci/
                    └── arsw/
                        └── primefinder/
                            ├── Main.java
                            ├── PrimeFinderThread.java
                            └── ThreadController.java
```

### 🔧 **Class Responsibilities**

#### 🎯 **Main.java**
- **Primary Role**: Application orchestration and user interaction
- **Features**:
  - 🚀 Thread creation and management
  - ⏱️ Timer-based pause scheduling
  - 📊 Progress reporting and final results
  - ⌨️ Console input handling

#### 🧵 **PrimeFinderThread.java**
- **Primary Role**: Prime number calculation within specified range
- **Features**:
  - 🔢 Efficient prime number detection algorithm
  - ⏸️ Pause point integration for synchronization
  - 📝 Thread-specific logging and identification
  - 🔒 Thread-safe state management

#### 🎮 **ThreadController.java**
- **Primary Role**: Thread synchronization and coordination
- **Features**:
  - 🔒 `synchronized` blocks for thread safety
  - 📡 `wait()` and `notifyAll()` primitives
  - 🛡️ State management for pause/resume operations
  - 🔄 Monitor pattern implementation

---

## 🔬 **Technical Implementation Details**

### 🧮 **Prime Number Algorithm**

```java
private boolean isPrime(int number) {
    if (number <= 1) return false;
    if (number == 2) return true;
    if (number % 2 == 0) return false;
    
    for (int i = 3; i * i <= number; i += 2) {
        if (number % i == 0) return false;
    }
    return true;
}
```

**Algorithm Characteristics:**
- ⚡ **Optimized checking**: Only odd numbers after 2
- 🎯 **Square root limit**: Efficient boundary checking
- 🔄 **Thread-safe**: No shared state mutations

### 🔄 **Synchronization Pattern**

```java
public void checkPausePoint() throws InterruptedException {
    synchronized (monitor) {
        while (isPaused) {
            monitor.wait();
        }
    }
}
```

**Synchronization Benefits:**
- 🛡️ **Thread safety**: Prevents race conditions
- ⚡ **Efficient waiting**: Avoids busy-waiting scenarios
- 🔄 **Coordinated resumption**: All threads resume simultaneously

---

## 📈 **Results & Conclusions**

### ✅ **Achievements**

1. **Thread Management Mastery**
   - ✅ Successfully implemented multi-threaded prime calculation
   - ✅ Achieved proper thread coordination using `join()`
   - ✅ Demonstrated significant performance improvements

2. **Synchronization Expertise**
   - ✅ Implemented pause/resume functionality using `wait()`/`notifyAll()`
   - ✅ Created thread-safe coordination mechanisms
   - ✅ Maintained data consistency across concurrent operations

3. **Performance Optimization**
   - ✅ Reduced execution time through parallelization
   - ✅ Optimized CPU core utilization
   - ✅ Balanced workload distribution among threads

### 🎯 **Key Learning Outcomes**

- **Concurrent Programming**: Understanding of thread creation, lifecycle, and coordination
- **Synchronization Primitives**: Practical application of *Java* synchronization mechanisms
- **Performance Analysis**: Measuring and comparing single vs multi-threaded execution
- **Resource Management**: Efficient utilization of system resources through threading

### 🔮 **Future Enhancements**

- 🎯 **Dynamic Thread Scaling**: Automatic thread count adjustment based on system resources
- 📊 **Advanced Metrics**: Real-time performance monitoring and reporting
- 🔄 **Load Balancing**: Dynamic work redistribution among threads
- 🛡️ **Exception Handling**: Enhanced error recovery and thread fault tolerance

---

## 🔗 **Additional Resources**

### 📚 **Documentation & References**

- [Java Threading Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/) - *Oracle's official concurrency guide*
- [Maven Exec Plugin](https://www.mojohaus.org/exec-maven-plugin/) - *Plugin documentation for project execution*
- [Java Synchronization](https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html) - *Thread synchronization mechanisms*

### 🎓 **Theoretical Foundations**

- [Concurrent Programming Concepts](https://en.wikipedia.org/wiki/Concurrent_computing) - *Fundamental concurrency principles*
- [Thread Synchronization Patterns](https://en.wikipedia.org/wiki/Synchronization_(computer_science)) - *Synchronization design patterns*
- [Performance Analysis in Parallel Computing](https://en.wikipedia.org/wiki/Parallel_computing) - *Performance evaluation techniques*

### 🛠️ **Development Tools**

- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - *Java IDE with threading debugging support*
- [Java VisualVM](https://visualvm.github.io/) - *Performance monitoring and profiling tool*
- [Maven](https://maven.apache.org/) - *Project management and build automation tool*
