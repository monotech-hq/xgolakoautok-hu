
(ns app.settings.frontend.cookie-consent.subs
    (:require [re-frame.api      :refer [r]]
              [x.environment.api :as x.environment]
              [x.router.api      :as x.router]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-consent?
  ; @return (boolean)
  [db _]
  (let [js-build (r x.router/get-current-js-build db)]
       (and (= js-build :app)
            (not (r x.environment/necessary-cookies-enabled? db)))))
