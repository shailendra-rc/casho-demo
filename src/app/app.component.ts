import { Component, NgZone } from '@angular/core';
import { Dialog } from '@capacitor/dialog';
import WebView from 'src/plugins/nativePlugins';
class ScraperConfig {
  constructor(
    url?: string,
    postLoginURL?: string,
    classes?: string[],
    ids?: string[],
    isAsync?: boolean
  ) {
    this.url = url;
    this.classes = classes;
    this.ids = ids;
    this.isAsync = isAsync;
    this.postLoginURL = postLoginURL;
  }
  url?: string;
  classes?: string[];
  ids?: string[];
  isAsync?: boolean;
  postLoginURL?: string;
}
class Product {
  constructor(obj?: any) {
    Object.assign(this, obj);
  }
  name?: string;
  date?: string;
  src?: string;
}
class Show {
  constructor(name?: string, date?: string) {
    this.name = name;
    this.date = date;
  }
  name?: string;
  date?: string;
}
class Ride {
  constructor(from?: string, to?: string, date?: string, status?: string) {
    this.from = from;
    this.to = to;
    this.date = date;
    this.status = status;
  }
  from?: string;
  to?: string;
  date?: string;
  status?: string;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'casho';
  products: Product[] = [];
  shows: Show[] = [];
  rides: Ride[] = [];
  constructor(private _ngZone: NgZone) {}

  openAmazon() {
    const scraperConfigs = [
      new ScraperConfig(
        'https://www.amazon.in/gp/your-account/order-history?unifiedOrders=0&digitalOrders=0&janeOrders=0&orderFilter=year-2022&ref_=ppx_yo2ov_mob_b_filter_y2022_all',
        'https://www.amazon.in/gp/css/order-history',
        [],
        ['ordersContainer'],
        false
      ),
      new ScraperConfig(
        'https://www.amazon.in/gp/your-account/order-history?unifiedOrders=0&digitalOrders=0&janeOrders=0&orderFilter=year-2021&ref_=ppx_yo2ov_mob_b_filter_y2021_all',
        'https://www.amazon.in/gp/css/order-history',
        [],
        ['ordersContainer'],
        false
      ),
      new ScraperConfig(
        'https://www.amazon.in/gp/your-account/order-history?unifiedOrders=0&digitalOrders=0&janeOrders=0&orderFilter=year-2020&ref_=ppx_yo2ov_mob_b_filter_y2020_all',
        'https://www.amazon.in/gp/css/order-history',
        [],
        ['ordersContainer'],
        false
      ),
      new ScraperConfig(
        'https://www.amazon.in/gp/your-account/order-history?unifiedOrders=0&digitalOrders=0&janeOrders=0&orderFilter=year-2019&ref_=ppx_yo2ov_mob_b_filter_y2019_all',
        'https://www.amazon.in/gp/css/order-history',
        [],
        ['ordersContainer'],
        false
      ),
    ];
    WebView.show(
      {
        scrapers: JSON.stringify(scraperConfigs),
      },
      (data) => {
        if (data?.data) {
          const htmlstring = data.data
            .replace(/\\u003C/g, '<')
            .replace(/\\"/g, "'")
            .replace(/\\n/g, '');
          const products: Product[] = [];
          var parser = new DOMParser();
          var test = parser.parseFromString(htmlstring, 'text/html');
          test
            .querySelectorAll('.a-section.a-padding-small.js-item')
            .forEach((element) => {
              let prod = new Product();
              prod.name =
                element.querySelector('.a-text-bold')?.innerHTML.trim() ?? '';
              prod.date =
                element
                  .querySelector('.a-size-small.a-color-secondary')
                  ?.innerHTML.trim() ?? '';
              prod.src =
                element
                  .querySelector('.yo-critical-feature')
                  ?.getAttribute('src') ?? '';
              products.push(prod);
            });
          this._ngZone.run(() => {
            this.products = products;
          });
        }
      }
    );
  }

  openNetflix() {
    const scraperConfigs = [
      new ScraperConfig(
        'https://www.netflix.com/settings/viewed/NKDVDV2YN5ANRA6APRM2JRSK4A',
        'https://www.netflix.com/settings/viewed/NKDVDV2YN5ANRA6APRM2JRSK4A',
        ['structural', 'retable', 'stdHeight'],
        [],
        false
      ),
      new ScraperConfig(
        'https://www.netflix.com/settings/viewed/J6THFU6HJVBFLDKUKWK3SFDORU',
        'https://www.netflix.com/settings/viewed/J6THFU6HJVBFLDKUKWK3SFDORU',
        ['structural', 'retable', 'stdHeight'],
        [],
        false
      ),
    ];
    WebView.show(
      {
        scrapers: JSON.stringify(scraperConfigs),
      },
      (data) => {
        if (data?.data) {
          const htmlstring = data.data
            .replace(/\\u003C/g, '<')
            .replace(/\\"/g, "'");
          const shows: Show[] = [];
          var netflixParser = new DOMParser();
          var testNetflix = netflixParser.parseFromString(
            htmlstring,
            'text/html'
          );
          testNetflix.querySelectorAll('.retableRow').forEach((x) => {
            let show = new Show();
            show.name = x.querySelector('.col.title>a')?.innerHTML.trim() ?? '';
            show.date =
              x.querySelector('.col.date.nowrap')?.innerHTML.trim() ?? '';
            shows.push(show);
          });
          this._ngZone.run(() => {
            this.shows = shows;
          });
        }
      }
    );
  }

  openUber() {
    const scraperConfigs = [
      new ScraperConfig(
        'https://riders.uber.com/trips',
        'https://riders.uber.com/trips',
        ['_css-gemfqT'],
        [],
        true
      ),
      new ScraperConfig(
        'https://riders.uber.com/trips?page=4',
        'https://riders.uber.com/trips?page=4',
        ['_css-gemfqT'],
        [],
        true
      ),
    ];
    WebView.show(
      {
        scrapers: JSON.stringify(scraperConfigs),
      },
      (data) => {
        if (data?.data) {
          const htmlstring = data.data
            .replace(/\\u003C/g, '<')
            .replace(/\\"/g, "'");
          var uberParser = new DOMParser();
          var testUber = uberParser.parseFromString(htmlstring, 'text/html');
          const rides: Ride[] = [];
          testUber.querySelectorAll('._css-gtxWCh').forEach((x) => {
            let ride = new Ride();
            ride.status =
              x.querySelector('._css-krCBTw')?.innerHTML.trim() ?? '';
            ride.date = x.querySelector('._css-dTqljZ')?.innerHTML.trim() ?? '';
            ride.from = x.querySelector('._css-jBkfju')?.innerHTML.trim() ?? '';
            ride.to = x.querySelector('._css-byJCfZ')?.innerHTML.trim() ?? '';
            rides.push(ride);
          });
          this._ngZone.run(() => {
            this.rides = rides;
          });
        }
      }
    );
  }
}
