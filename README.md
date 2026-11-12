# schedule_helper

Java
Spring Boot
Telegram

Telegram bot for university class schedules and current weather

This is a Telegram bot built with Java and Spring Boot that shows current weather and university class schedules. It uses the TelegramBots API for user interaction and fetches data from external REST APIs (OpenWeatherMap and the KPI Campus schedule API) via `RestTemplate` and Jackson. The code follows a layered architecture: a controller routes messages, while the service layer handles external requests and data processing.

- Current weather in Kyiv
- Class schedule for today
- Class schedule for tomorrow

Bot commands:
-------------------

- `Погода` - current weather
- `Розклад` / `Розклад на сьогодні` - today's schedule
- `Розклад на завтра` - tomorrow's schedule

Requirements:
-------------------

- JDK 17+
- Maven
- Telegram bot token from [@BotFather](https://t.me/BotFather)
- OpenWeatherMap API key

Get the source code:
-------------------
Clone the repository:

	$ git clone https://github.com/ishtefanovych/schedule_helper.git

To build the application:
-------------------

Set your bot token in `TelegramBot.java` and your OpenWeatherMap key in `WeatherService.java`, then run:

	$ cd schedule_helper/schedule_helper
	$ mvn clean install
	$ mvn spring-boot:run

### Access the application:
-------------------

To get the bot up and running, simply launch the application and open the bot in Telegram by its handle, **@ShtefanScheduleBot**.

Run with Docker:
-------------------

1. Create a `.env` file in the repository root (do not commit it):

```
TELEGRAM_BOT_TOKEN=your_telegram_bot_token
WEATHER_API_KEY=your_openweathermap_key
```

2. Build the image:

```
docker build -t schedule-helper .
```

3. Start the container:

```
docker run -d --name schedule-bot --restart unless-stopped --env-file .env schedule-helper
```

4. Check the logs:

```
docker logs -f schedule-bot
```

Stop / start / remove the container:

```
docker stop schedule-bot
docker start schedule-bot
docker rm -f schedule-bot
```

### Future plans:
-------------------

Automated daily updates using Spring scheduling.
