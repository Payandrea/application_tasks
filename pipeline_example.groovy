pipeline {
  agent any
  //agent { label 'agent_name' } Descomentar si hay un agente que lo corra

  //Marcas de tiempo de compilación para cada línea
  options {
    timeout(time: 60, unit: 'MINUTES')
    timestamps()
  }

  stages {
              stage('Git-Checkout') {
            steps {
                echo "Checking out from Git Repo";
                git branch: 'main', url: 'https://github.com/Payandrea/Pipeline_Script.git'
            }
        }

        stage('Build') {
            steps {
                echo "Building the checked-out project";
                bat 'Build.bat'
            }
        }

        stage('Unit-Test') {
            steps {
                echo "Running JUnit Tests";
                bat 'Unit.bat'
            }
        }

        stage('Quallity-Gate') {
            steps {
                echo "Verifying Quality Gates";
                bat 'Quality.bat'
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying to Stage Enviroment for more tests!";
                bat 'Deploy.bat'
            }
        }
  }

  post {
    success {
      echo "SUCCESS"
    }
    failure {
      echo "FAILURE"
    }
    unstable {
      echo "Tasks unstable, please check it :)"
    }
    changed {
      echo "Status Changed: [From: $currentBuild.previousBuild.result, To: $currentBuild.result]"
    }
    always {
      script {
        def result = currentBuild.result
        if (result == null) {
          result = "SUCCESS"
        }
      }
    }
  }
} 
