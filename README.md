# tweetsInjector

##How to use:
- NEO4J:
    - Before running the app how should have a Neo4j Graph running and with the following configuration:
    
        url = "bolt://localhost:7687"; (alternatively this can be changed in the file "ConnectService.java")   
        user = "neo4j"   
        password = "1234"   
    
    - Open the project using IntellijIDEA and then run the main method followed by arguments
        "src/main/java/ch/usi/si/msde/ddm/tweetsinjector/app/App.java -db=NEO4J"  

- MONGODB:
    
     - Open the project using IntellijIDEA and then run the main method followed by arguments
        "src/main/java/ch/usi/si/msde/ddm/tweetsinjector/app/App.java -db=MONGODB"  
