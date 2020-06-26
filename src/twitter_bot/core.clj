(ns twitter-bot.core
  (:require [twitter.oauth :as oauth]
            [twitter.api.restful :as rest]))


(defonce app-consumer-key         (System/getenv "TWITTER_CONSUMER_KEY"))
(defonce app-consumer-secret      (System/getenv "TWITTER_CONSUMER_SECRET"))
(defonce user-access-token        (System/getenv "TWITTER_ACCESS_TOKEN"))
(defonce user-access-token-secret (System/getenv "TWITTER_ACCESS_TOKEN_SECRET"))


(def creds (oauth/make-oauth-creds
            app-consumer-key
            app-consumer-secret
            user-access-token
            user-access-token-secret))


(def naval-messages
  (->> "naval.txt"
       clojure.java.io/reader
       line-seq
       (take-nth 2)
       vec))


(defn tweet [msg]
  (rest/statuses-update :oauth-creds creds :params {:status msg}))


(defn send-tweet [msg-file]
  (try
    (let [resp (tweet msg)]
      (println (:status resp)))
    (catch Throwable t
      (println (.getMessage t)))
    (finally
      (System/exit 0))))


(defn -main []
  (send-tweet (rand-nth naval-messages) creds))
