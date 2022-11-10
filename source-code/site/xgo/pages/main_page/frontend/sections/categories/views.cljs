
(ns site.xgo.pages.main-page.frontend.sections.categories.views
  (:require [re-frame.api :as r]
            [mid-fruits.normalize :as normalize]
            [site.xgo.pages.main-page.frontend.sections.categories.subs]))

(defn normalize-str [text]
  (-> text (str)
           (normalize/deaccent)
           (normalize/cut-special-chars "-+")
           (normalize/space->separator)
           (clojure.string/lower-case)))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- category-button [{:category/keys [id name] :as category}]
  [:button {:key           id
            :class         "xgo-category--button"
            :on-click      #(r/dispatch [::select! name])
            :data-selected @(r/subscribe [:category.this/selected? (normalize-str name)])}
   name])

(defn- categories [{:keys [categories]}]
  [:<>
    (doall (map category-button categories))])

(defn view []
  (let [view-props {:categories @(r/subscribe [:db/get-item [:site :categories]])}]
    [:div (str @(r/subscribe [:db/get-item [:filters]]))
      [:section {:id "xgo-categories"}
                [categories view-props]]]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-event-fx
  ::select!
  (fn [_ [_ name]]
    {:dispatch [:db/set-item! [:filters :category] (normalize-str name)]}))
     ; :url/set-url!}))
