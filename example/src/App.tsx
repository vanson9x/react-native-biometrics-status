import { hasChanged } from 'react-native-biometrics-status';

export default function App() {
  hasChanged().then((next) => console.log('hasChanged:', next));
  return null;
}
