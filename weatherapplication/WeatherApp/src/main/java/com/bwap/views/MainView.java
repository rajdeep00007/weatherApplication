package com.bwap.weatherapp.WeatherApp.views;

import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
//import com.fasterxml.jackson.databind.util.JSONPObject;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
//import elemental.json.JsonArray;
//import elemental.json.JsonException;
//import elemental.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@SpringUI(path= "")
public class MainView extends UI{

    @Autowired
    private WeatherService weatherService;

    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button btn;
    private HorizontalLayout dashboard;
    private Label location;
    private Label currentTemp;
    private HorizontalLayout mainDescriptionLayout;
    private Label weatherDescription;
    private Label MinWeather;
    private Label MaxWeather;
    private Label Humidity;
    private Label Pressure;
    private Label Wind;
    private Label FeelsLike;
    private Image iconImg;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();
        btn.addClickListener(clickEvent -> {
            if(!cityTextField.getValue().equals("")){
                try {
                    updateUI();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                Notification.show("Please enter the city name");
            }
        });
    }

    private void mainLayout() {
        iconImg = new Image();
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
    }

    private void setHeader(){
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title = new Label("* Weather Application *");

        header.addComponent(title);

        mainLayout.addComponents(header);
    }

    private void setLogo(){
        HorizontalLayout logo =new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img = new Image(null,new ClassResource("/static/logo.png"));
        logo.setWidth("240px");
        logo.setHeight("240px");

        logo.addComponent(img);
        mainLayout.addComponents(logo);
    }

    private void setForm(){
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        unitSelect = new NativeSelect<>();
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponent(unitSelect);

        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponent(cityTextField);

        btn = new Button();
        btn.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(btn);

        mainLayout.addComponents(formLayout);
    }

    private void dashboardTitle(){
        dashboard = new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        location = new Label("Currently in Rajkot ");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        currentTemp = new Label("10 C");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);

        dashboard.addComponents(location,iconImg,currentTemp);

    }

    private void dashboardDetails(){
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout descriptionlayout = new VerticalLayout();
        descriptionlayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        weatherDescription = new Label("DescriptionL: Clear skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        descriptionlayout.addComponents(weatherDescription);

        MinWeather = new Label("Min:53");
        descriptionlayout.addComponents(MinWeather);

        MaxWeather = new Label("Max:53");
        descriptionlayout.addComponents(MaxWeather);

        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Pressure = new Label("Pressure:231Pa");
        pressureLayout.addComponents(Pressure);

        Humidity = new Label("Humidity:23");
        pressureLayout.addComponents(Humidity);

        Wind = new Label("Wind:231");
        pressureLayout.addComponents(Wind);

        FeelsLike = new Label("FeelsLike:23");
        pressureLayout.addComponents(FeelsLike);

        mainDescriptionLayout.addComponents(descriptionlayout,pressureLayout);

    }

    private void updateUI() throws JSONException {
        String city = cityTextField.getValue();
        String defaultUnit;
        weatherService.setCityname(city);

        if(unitSelect.getValue().equals("F")){
            weatherService.setUnit("imperials");
            unitSelect.setValue("F");
            defaultUnit = "\u00b0"+"F";
        }else{
            weatherService.setUnit("metric");
            defaultUnit = "\u00b0"+"C";
            unitSelect.setValue("C");
        }

        location.setValue("Currently in "+city);
        JSONObject mainObject = weatherService.returnMain();
        int temp = mainObject.getInt("temp");
        currentTemp.setValue(temp+defaultUnit);

        String iconCode = null;
        String weatherDescriptionNew = null;
        JSONArray jasonArray = weatherService.returnWeatherArray();
        for (int i=0; i<jasonArray.length();i++){
            JSONObject weatherObj = jasonArray.getJSONObject(i);
            iconCode = weatherObj.getString("icon");
            weatherDescriptionNew = weatherObj.getString("description");
        }

        iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));

        weatherDescription.setValue("Description: " +weatherDescriptionNew);
        MinWeather.setValue("Min temp: "+weatherService.returnMain().getInt("temp_min")+unitSelect.getValue());
        MaxWeather.setValue("Max temp: "+weatherService.returnMain().getInt("temp_max")+unitSelect.getValue());
        Pressure.setValue("Pressure: "+weatherService.returnMain().getInt("pressure"));
        Humidity.setValue("Humidity: "+weatherService.returnMain().getInt("humidity"));
        Wind.setValue("Wind: "+weatherService.returnWind().getInt("speed"));
        FeelsLike.setValue("Feels Like "+weatherService.returnMain().getDouble("feels_like"));

        mainLayout.addComponents(dashboard,mainDescriptionLayout);

    }

}
