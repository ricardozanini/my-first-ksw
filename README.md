# Getting Started with Kogito Serverless Workflow

This guide quickly explains how to getting started with Kogito Serverless Workflows in your local machine

## Prereqs

1. [Java SDK installed](https://adoptopenjdk.net/)
2. [Maven installed](https://maven.apache.org/install.html)
3. [Quarkus CLI](https://quarkus.io/guides/cli-tooling)

To edit your workflows:

1. Visual Studio Code with [Red Hat Java Plugin](https://marketplace.visualstudio.com/items?itemName=redhat.java) installed
2. [Serverless Workflow Editor](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-extension-serverless-workflow-editor)

## Creating the project

To create the project skeleton, run:

```shell
quarkus create app -x=kogito-quarkus-serverless-workflow,quarkus-container-image-jib,quarkus-resteasy-jackson,quarkus-smallrye-openapi  org.acme:my-first-ksw:1.0
```

The `org.acme:my-first-ksw:1.0` is the group id, artifact id, and version of your project.

This command will create a Maven Quarkus project in the `my-first-ksw` directory with all required Kogito dependencies.

Next, to make sure everything is working fine, try to compile the project with:

```shell
quarkus build
```

## Creating your first Workflow

Go to the directory `src/main/resources` and create a file named `greetings.sw.yaml`. 
You can play around and type the workflow definition by hand using the editor intellisense feature or copy and paste from the snnipet below:

```yaml
id: greetings
version: '1.0'
name: Greeting workflow
description: YAML based greeting workflow
start: ChooseOnLanguage
functions:
  - name: greetFunction
    type: custom
    operation: sysout
states:
  - name: ChooseOnLanguage
    type: switch
    dataConditions:
      - condition: ${.language == \"English\"}
        transition: GreetInEnglish
      - condition: ${.language == \"Spanish\"}
        transition: GreetInSpanish
    defaultCondition:
      transition: GreetInEnglish
  - name: GreetInEnglish
    type: inject
    data:
      greeting: 'Hello from YAML Workflow, '
    transition: GreetPerson
  - name: GreetInSpanish
    type: inject
    data:
      greeting: 'Saludos desde YAML Workflow, '
    transition: GreetPerson
  - name: GreetPerson
    type: operation
    stateDataFilter:
      input: |-
       {greeting: .greeting + .name}
    actions:
      - name: greetAction
        functionRef:
          refName: greetFunction
          arguments:
            message: ${.greeting}
    end: true
```

Then run `quarkus dev` from the project's root to start the Quarkus console.

To interact with the application, we have a couple of options described in the sections below.

### Using the Swagger UI

Point your browser to the http://localhost:8080/q/swagger-ui/ address. 
You should see a POST endpoint definition for this workflow named `Greetings`. Click on the POST entry and in the button "Try it".

Copy and paste the following content:

```json
{
  "workflowdata": {
   "name" : "John",
   "language": "English"
  }
}
```

Click on "Execute" and you should see a response similarly to this one:

```json
{
  "id": "c758eb9d-6242-4bda-a5f5-7f5d302943c0",
  "workflowdata": {
    "greeting": "Hello from YAML Workflow, John",
  }
}
```

### Calling the service via command line

You can use an one line `curl`:

```shell
curl -X POST -H 'Content-Type:application/json' -H 'Accept:application/json' -d '{"workflowdata" : {"name": "John", "language": "English"}}' http://localhost:8080/greetings
```

### Building your project's image

You can use the Quarkus CLI to build your image with the following command:

```shell
quarkus build -Dquarkus.container-image.build=true
```

After building the image, you can start the container running:

```shell
docker run --rm -it -p8080:8080 <username>/my-first-ksw:1.0
```

You can then interact with the application the same way you did before in the previous sections.

## Resources

- [Quarkus Container Images Guide](https://quarkus.io/guides/container-image)
- [Getting Started With Quarkus](https://quarkus.io/guides/getting-started)
- [CNCF Serverless Workflow](https://serverlessworkflow.io/)
- [Kogito Serverless Workflow](https://github.com/kiegroup/kogito-runtimes/tree/main/kogito-serverless-workflow)