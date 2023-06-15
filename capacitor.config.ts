import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.example.app',
  appName: 'casho',
  webDir: 'dist',
  server: {
    url: 'https://c4e8-2409-40c1-2f-63db-fdbd-5da-190b-2cc6.ngrok-free.app/',
    cleartext: true,
  },
};

export default config;
