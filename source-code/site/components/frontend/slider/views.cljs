(ns site.components.frontend.slider.views
  (:require ["react-responsive-carousel" :refer [Carousel]]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(def DEFAULT-CONFIG {:emulateTouch   true
                     :infiniteLoop   true
                     :showThumbs     false
                     :showIndicators false
                     :showStatus     false})

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn slider
  [data]
  (let [configurations DEFAULT-CONFIG]
    [:div [:> Carousel configurations
              data]]))

(defn component
  [& data]
  [:div [slider data]])
