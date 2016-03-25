module.exports = function(grunt) {
    require('time-grunt')(grunt);
    require('jit-grunt')(grunt);

    // Keep the plugins in alphabetical order
    grunt.initConfig({
        babel: {
            options: {
                sourceMap: true,
                plugins: ['rewire'],
                presets: ['es2015-node5']
            },
            main: {
                files: [{
                    expand: true,
                    cwd: 'src/main',
                    src: ['**/*.js'],
                    dest: 'target/mud'
                }]
            }
        },
        clean: ['target'],
        eslint: {
            options: {
                configFile: 'eslintrc'
            },
            main: {
                files: [{
                    expand: true,
                    src: ['src/main/**/*.js']
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
            }
        },
        mochaTest: {
            main: {
                src: [
                    'target/mud/**/*.spec.js'
                ],
                options: {
                    reporter: 'spec',
                    growl: true
                }
            }
        },
        notify: {
            watch_start: {
                options: {
                    message: 'Rebuild and restart complete'
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

    grunt.registerTask('build', ['eslint', 'jscpd', 'babel', 'mochaTest']);
    grunt.registerTask('start', ['build', 'execute:main']);
};
