
(ns site.components.frontend.navbar.views
    (:require [elements.api                                 :as elements]
              [hiccup.api                                   :as hiccup]
              [random.api                                   :as random]
              [re-frame.api                                 :as r]
              [site.components.frontend.navbar.helpers      :as navbar.helpers]
              [site.components.frontend.scroll-sensor.views :as scroll-sensor.views]
              [x.components.api                             :as x.components]
              [x.environment.api                            :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- navbar-menu-button
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:on-menu (metamorphic-event)(opt)}
  [component-id {:keys [on-menu]}]
  (if on-menu [:button {:id :mt-navbar--menu-button
                        :data-clickable true
                        :on-click    #(r/dispatch on-menu)
                        :on-mouse-up #(x.environment/blur-element!)}
                       [:span]]))

(defn- navbar-menu-items
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:menu-items (maps in vector)
  ;    {:class (keyword or keywords in vector)(opt)
  ;     :href (string)
  ;     :label (metamorphic-content)
  ;     :style (map)(opt)
  ;     :target (string)(opt)}}
  [component-id {:keys [menu-items]}]
  (letfn [(f [menu-items {:keys [class href label style target]}]
             (conj menu-items [:a {:class (hiccup/join-class :mt-navbar--menu-item class)
                                   :style style :href href :target (or target "_self")
                                   :on-mouse-up #(x.environment/blur-element!)}
                                  (x.components/content label)]))]
         [:div {:id :mt-navbar--menu-items}
               (reduce f [:<>] menu-items)]))

(defn- navbar-logo
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:logo (metamorphic-content)(opt)}
  [component-id {:keys [logo]}]
  (if logo [:div {:id :mt-navbar--logo}
                 [x.components/content component-id logo]]))

(defn- navbar
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :style (map)(opt)
  ;   :threshold (px)(opt)}
  [component-id {:keys [class style threshold] :as component-props}]
  (let [threshold? (<= threshold @(r/subscribe [:x.environment/get-viewport-width]))]
       [:<> [scroll-sensor.views/component ::scroll-sensor {:callback-f navbar.helpers/scroll-f
                                                            :style {:left "0" :position "absolute" :top "0"}}]
            [:div {:id :mt-navbar :class class :style style
                   :data-profile (if threshold? :desktop :mobile)}
                  [navbar-logo component-id component-props]
                  (if threshold? [navbar-menu-items  component-id component-props]
                                 [navbar-menu-button component-id component-props])]]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :logo (metamorphic-content)(opt)
  ;   :menu-items (maps in vector)
  ;    {:class (keyword or keywords in vector)(opt)
  ;     :href (string)
  ;     :label (metamorphic-content)
  ;     :style (map)(opt)
  ;     :target (string)(opt)
  ;      Default: "_self"}
  ;   :on-menu (metamorphic-event)(opt)
  ;   :style (map)(opt)
  ;   :threshold (px)(opt)
  ;    Milyen széles viewport számít desktop nézetnek }
  ;
  ; @usage
  ;  [navbar {...}]
  ;
  ; @usage
  ;  [navbar :my-navbar {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [navbar component-id component-props]))
