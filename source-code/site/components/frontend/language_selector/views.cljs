
(ns site.components.frontend.language-selector.views
    (:require [random.api   :as random]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- language-selector
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:languages (keywords in vector)
  ;   :style (map)(opt)}
  [_ {:keys [languages style]}]
  (let [selected-language @(r/subscribe [:x.locales/get-selected-language])]
       [:div {:id :mt-language-selector :style style}
             (letfn [(f [languages language]
                        (conj languages [:div {:class :mt-language-selector--language
                                               :data-selected (= language selected-language)
                                               :on-click #(r/dispatch [:components.language-selector/select-language! language])}
                                              (name language)]))]
                    (reduce f [:<>] languages))]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:languages (keywords in vector)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [language-selector {...}]
  ;
  ; @usage
  ;  [language-selector :my-language-selector {...}]
  ;
  ; @usage
  ;  [language-selector :my-language-selector
  ;                     {:languages [:en :hu]}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [language-selector component-id component-props]))
