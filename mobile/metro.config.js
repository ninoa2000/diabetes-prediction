const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// WORKAROUND for Node 24 + Windows path error
config.resolver.unstable_enablePackageExports = false;
config.resolver.unstable_conditionNames = ['require', 'import', 'react-native'];
config.resolver.sourceExts = [...config.resolver.sourceExts, 'mjs'];

module.exports = config;
