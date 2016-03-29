module.exports = function(grunt) {
    require('time-grunt')(grunt);
    require('jit-grunt')(grunt);

    // Keep the plugins in alphabetical order
    grunt.initConfig({
        babel: {
            options: {
                sourceMap: true,
                plugins: [
                    'rewire',
                    'transform-regenerator',
                    'syntax-async-functions'
                ],
                presets: ['es2015']
            },
            main: {
                files: [{
                    expand: true,
                    cwd: 'src/main',
                    src: ['**/*.js'],
                    dest: 'target/mud'
                }]
            },
            cucumber: {
                files: [{
                    expand: true,
                    cwd: 'src/cucumber/steps',
                    src: ['**/*.js'],
                    dest: 'target/cucumber/steps'
                }]
            }
        },
        clean: ['target'],
        cucumberjs: {
            options: {
                theme: 'bootstrap',
                output: 'target/features_report.html',
                format: 'pretty',
                saveJson: true
            },
            main: {
                options: {
                    steps: 'target/cucumber/steps',
                },
                src: [
                    'src/cucumber/features'
                ]
            }
        },
        eslint: {
            options: {
                configFile: 'eslintrc'
            },
            main: {
                files: [{
                    expand: true,
                    src: ['src/main/**/*.js']
                }]
            },
            cucumber: {
                files: [{
                    expand: true,
                    src: ['src/cucumber/steps/**/*.js']
                }]
            }
        },
        execute: {
            main: {
                src: 'target/mud/index.js'
            }
        },
        jscpd: {
            main: {
                path: 'src/main'
            },
            cucumber: {
                path: 'src/cucumber/steps'
            }
        },
        mochaTest: {
            main: {
                src: [
                    'target/mud/**/*.spec.js'
                ],
                options: {
                    reporter: 'spec',
                    growl: true,
                    require: 'target/mud/testSetup'
                }
            }
        },
        watch: {
            build: {
                files: [
                    'src/**/*',
                    'Gruntfile.js',
                    'eslintrc'
                ],
                tasks: ['build'],
                options: {
                    interrupt: true,
                    atBegin: true
                }
            }
        }
    });

    grunt.registerTask('build', ['eslint:main', 'jscpd:main', 'babel:main', 'mochaTest']);
    grunt.registerTask('buildCucumber', ['eslint:cucumber', 'jscpd:cucumber', 'babel:cucumber']);
    grunt.registerTask('cucumber', ['build', 'buildCucumber', 'cucumberjs']);
    grunt.registerTask('start', ['build', 'execute:main']);
};
