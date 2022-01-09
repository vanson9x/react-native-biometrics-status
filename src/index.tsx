import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-biometrics-status' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const BiometricsStatus = NativeModules.BiometricsStatus
  ? NativeModules.BiometricsStatus
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function reset(): Promise<boolean> {
  return BiometricsStatus.reset();
}

export function hasChanged(): Promise<boolean | string | undefined> {
  return BiometricsStatus.hasChanged();
}
