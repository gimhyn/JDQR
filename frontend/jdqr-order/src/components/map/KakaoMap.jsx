import React, { useEffect, useState } from "react"
import { Stack } from "@mui/material"
import { Map, MapMarker, useKakaoLoader } from "react-kakao-maps-sdk"
import activeMapmarker from "../../assets/images/mapmarker1.png"
import inactiveMapmarker from "../../assets/images/mapmarker2.png"

const KakaoMap = ({
  onBoundsChange,
  initialLocation,
  initialBounds,
  restaurants,
}) => {
  const [isActive, setIsActive] = useState(false)
  const [location, setLocation] = useState({
    lat: 37.50125774784631,
    lng: 127.03956684373539,
  })
  const [bounds, setBounds] = useState(initialBounds || null)

  const isLoaded = useKakaoLoader()

  useEffect(() => {
    if (isLoaded && initialLocation) {
      setLocation(initialLocation)
    }
    if (initialBounds) {
      setBounds(initialBounds)
    }
  }, [isLoaded, initialLocation, initialBounds])

  const handleBoundsChanged = (map) => {
    const bounds = map.getBounds()
    const sw = bounds.getSouthWest()
    const ne = bounds.getNorthEast()

    onBoundsChange({
      minLat: sw.getLat(),
      maxLat: ne.getLat(),
      minLng: sw.getLng(),
      maxLng: ne.getLng(),
    })
  }

  return (
    <Stack sx={{ width: "100%", height: "100%" }}>
      <Map
        id="map"
        center={location}
        style={{
          top: 0,
          width: "100%",
          height: "100%",
        }}
        level={3}
        draggable
        scrollwheel
        onDragEnd={(map) => handleBoundsChanged(map)}
        onZoomChanged={(map) => handleBoundsChanged(map)}
        bounds={bounds}
      >
        {/* 기본 위치 마커 (현재 위치) */}
        <MapMarker
          position={location}
          image={{
            src: "https://cdn-icons-png.flaticon.com/128/2098/2098567.png",
            size: {
              width: 35,
              height: 35,
            },
          }}
        />

        {/* restaurants가 존재할 때 각 식당들의 위치에 마커 추가 */}
        {restaurants &&
          restaurants.map((restaurant) => (
            <MapMarker
              key={restaurant.id}
              position={{
                lat: restaurant.lat,
                lng: restaurant.lng,
              }}
              image={{
                src: isActive ? activeMapmarker : inactiveMapmarker,
                size: { width: 80, height: 60 },
              }}
              // onClick={() => { // 추후 로직 수정
              //   setIsActive(!isActive)
              //   console.log(
              //     `Clicked on restaurant: ${restaurant.restaurantName}`
              //   )
              // }}
            />
          ))}
      </Map>
    </Stack>
  )
}

export default KakaoMap