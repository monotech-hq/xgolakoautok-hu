
(ns app.components.frontend.context-menu.views
    (:require [elements.api        :as elements]
              [layouts.popup-a.api :as popup-a]
              [random.api          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- context-menu-item
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; @param (map) menu-item
  ;  {:color (keyword or string)(opt)
  ;    Default: :default
  ;   :label (metamorphic-content)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [_ _ {:keys [color label on-click placeholder]}]
  [elements/button {:color            (or color :default)
                    :horizontal-align :left
                    :hover-color      :highlight
                    :indent           {:vertical :xs}
                    :label            label
                    :on-click         {:dispatch-n [[:x.ui/remove-popup! :components.context-menu/view] on-click]}
                    :placeholder      placeholder}])

(defn- context-menu-body
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;  {:menu-items (maps in vector)}
  [menu-id {:keys [menu-items] :as menu-props}]
  (letfn [(f [menu-items menu-item] (conj menu-items [context-menu-item menu-id menu-props menu-item]))]
         [:<> (reduce f [:<>] menu-items)
              [elements/horizontal-separator {:height :xs}]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- close-icon-button
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [_ _]
  [elements/icon-button ::close-icon-button
                        {:hover-color :highlight
                         :keypress    {:key-code 27}
                         :on-click    [:x.ui/remove-popup! :components.context-menu/view]
                         :preset      :close}])

(defn- header-label
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [_ {:keys [label placeholder]}]
  [elements/label ::header-label
                  {:color       :muted
                   :content     label
                   :font-size   :xs
                   :indent      {:horizontal :xs :left :s}
                   :line-height :block
                   :placeholder placeholder}])

(defn- context-menu-header
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [elements/horizontal-polarity ::context-menu-header
                                {:start-content [header-label      menu-id menu-props]
                                 :end-content   [close-icon-button menu-id menu-props]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- context-menu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [popup-a/layout :components.context-menu/view
                  {:body   [context-menu-body   menu-id menu-props]
                   :header [context-menu-header menu-id menu-props]
                   :min-width :xs}])

(defn component
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ;  {:label (metamorphic-content)(opt)
  ;   :menu-items (maps in vector)
  ;    [{:color (keyword or string)(opt)
  ;       Default: :default
  ;      :label (metamorphic-content)(opt)
  ;      :on-click (metamorphic-event)(opt)
  ;      :placeholder (metamorphic-content)(opt)}]
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [context-menu {...}]
  ;
  ; @usage
  ;  [context-menu :my-context-menu {...}]
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (let []; menu-props (context-menu.prototypes/menu-props-prototype menu-props)
        [context-menu menu-id menu-props])))
