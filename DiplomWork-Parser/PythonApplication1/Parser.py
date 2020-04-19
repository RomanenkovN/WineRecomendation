import requests
from bs4 import BeautifulSoup
import csv
import json


def get_html(url):
    r = requests.get(url)
    return r.text


def get_total_pages(html):
    soup = BeautifulSoup(html, 'lxml')
    divs = soup.find('div', class_='paginations paginations--s_1')
    pages = divs.find_all('a', class_='paginations__link')[-2].get('href')
    total_pages = pages.split('=')[1].split('&')[0]
    return int(total_pages)


def write_csv(data):
    with open('wine.json', 'a') as f:
        writer = csv.writer(f)
        writer.writerow((data['Color'],
                         data['Country'],
                         data['Description'],
                         data['Maker'],
                         data['Name'],
                         data['Region'],
                         data['Sort'],
                         data['Sweetness'],
                         data['Year']))


def get_page_data(html, base_url_for_wine):
    soup = BeautifulSoup(html, 'lxml')
    cur = soup.find('li', class_='paginations__item paginations__item--active ')
    print(cur.text.strip())
    divs = soup.find('div', class_='catalog-list__list mb30')
    ads = divs.find_all('div', class_='prod-card prod-card--s_1')
    for ad in ads:
        try:
            url = ad.find('a', class_='prod-card__title').get('href')
            r = requests.get(base_url_for_wine + url)
            soup_wine = BeautifulSoup(r.text, 'lxml')
            name_div = soup_wine.find('h1', class_='heading')
            name = name_div.text.split(',')[0].strip()
            print(name)
            divs_wine = soup_wine.find_all('div', class_='card-top-slider__descr-list')
        except:
            continue
        try:
            div = divs_wine[0].find_all('a', class_='card-top-slider__descr-link')
            color = div[0].text.strip()
            print(color)
        except:
            color = ''
            print(color)
        try:
            div = divs_wine[1].find_all('a', class_='card-top-slider__descr-link')
            try: 
                country = div[0].text.split(',')[0].strip()
            except:
                country = ''
            try:
                region = div[1].text.strip()
            except:
                region = ''
            print(country)
            print(region)
        except:
            country = ''
            region = ''
            print(country)
            print(region)
        try:
            div = soup_wine.find_all('tbody')
            description = div[0].text.strip()
            print(description)
        except:
            description = ''
            print(description)
        try:
            div = divs_wine[4].find_all('a', class_='card-top-slider__descr-link')
            maker = div[0].text.strip()
            print(maker)
        except:
            maker = ''
            print(maker)
        try:
            div = divs_wine[3].find_all('a', class_='card-top-slider__descr-link')
            sort = div[0].text.strip()
            print(sort)
        except:
            sort = ''
            print(sort)
        try:
            div = divs_wine[2].find_all('a', class_='card-top-slider__descr-link')
            sweetness = div[0].text.strip()
            print(sweetness)
        except:
            sweetness = ''
            print(sweetness)
        try:
            if (int(name_div.text.split(',')[1].strip())):
                year = name_div.text.split(',')[1].strip()
                print(year)
        except:
            year = ''
        data = {'Color':color,
                'Country':country, 
                'Description': description,
                'Maker':maker,
                'Name':name,
                'Region':region, 
                'Sort':sort,
                'Sweetness':sweetness,
                'Year':year}
        write_csv(data)


def main():
    base_url = "https://moscow.altavina.ru/catalog/wine"
    base_url_for_wine = "https://altavina.ru"
    page_part = "/?PAGEN_1="

    total_pages = get_total_pages(get_html(base_url))
    print(total_pages)

    for i in range(2, 3):
        url_gen = base_url + page_part + str(i)
        html = get_html(url_gen)
        get_page_data(html, base_url_for_wine)


if __name__ == '__main__':
    main()